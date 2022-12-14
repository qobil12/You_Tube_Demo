package com.company.service;

import com.company.config.CustomUserDetails;
import com.company.dto.TagDTO;
import com.company.dto.VideoFullInfoDTO;
import com.company.repository.VideoWatchedRepository;
import com.company.repository.mapper.VideoViewLikeDislikeCountAndStatusByProfile;
import com.company.dto.category.CategoryVideoDTO;
import com.company.dto.playlist.PlaylistVideoDTO;
import com.company.dto.video.VideoDTO;
import com.company.dto.video.VideoShortInfoDTO;
import com.company.dto.video.VideoStatusDTO;
import com.company.dto.video.VideoUpdateDTO;
import com.company.entity.AttachEntity;
import com.company.entity.CategoryEntity;
import com.company.entity.ProfileEntity;
import com.company.entity.video.VideoEntity;
import com.company.entity.video.VideoTagEntity;
import com.company.enums.LikeStatus;
import com.company.enums.ProfileRole;
import com.company.exceptions.ItemNotFoundException;
import com.company.exceptions.MethodNotAllowedException;
import com.company.exceptions.NoPermissionException;
import com.company.repository.VideoRepository;
import com.company.repository.VideoTagRepository;
import com.company.repository.mapper.VideoFullInfo;
import com.company.repository.mapper.VideoShortInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class VideoService {
    @Autowired

    private VideoRepository videoRepository;
    @Autowired

    private VideoTagRepository videoTagRepository;
    @Autowired

    private VideoWatchedRepository videoWatchedRepository;

    @Autowired
    @Lazy
    private AttachService attachService;

    @Autowired
    @Lazy
    private CategoryService categoryService;

    @Autowired
    @Lazy
    private ChannelService channelService;

    @Autowired
    @Lazy
    private PlaylistService playlistService;

    @Autowired
    @Lazy
    private PlaylistVideoService playlistVideoService;

    @Autowired
    @Lazy
    private CategoryVideoService categoryVideoService;

    @Autowired
    private ProfileService profileService;


    public boolean existsPhoto(String id) {
        return videoRepository.existsByAttachIdOrPreviewId(id, id);
    }

    public String create(VideoDTO dto) {
        if (!attachService.exists(dto.getAttachId())) {
            log.error("Attach not found!");
            throw new ItemNotFoundException(
                    "Attach not found!"
            );
        }

        if (!attachService.exists(dto.getPreviewId())) {
            log.error("Preview not found!");
            throw new ItemNotFoundException(
                    "Preview not found!"
            );
        }

        if (!channelService.exists(dto.getChannelId())) {
            log.error("Channel not found!");
            throw new ItemNotFoundException(
                    "Channel not found!"
            );
        }

        if (!playlistService.exists(dto.getPlaylistId())) {
            log.error("Playlist not found!");
            throw new ItemNotFoundException(
                    "Playlist not found!"
            );
        }

        if (!categoryService.exists(dto.getCategoryId())) {
            log.error("Category not found!");
            throw new ItemNotFoundException(
                    "Category not found!"
            );
        }

        VideoEntity entity = toEntity(dto);

        videoRepository.save(entity);

        createPlaylistVideo(entity, dto);

        createCategoryVideo(entity, dto);

        return "Successfully saved a video";
    }

    private void createCategoryVideo(VideoEntity entity, VideoDTO dto) {
        CategoryVideoDTO categoryVideoDTO = new CategoryVideoDTO();
        categoryVideoDTO.setCategoryId(dto.getCategoryId());
        categoryVideoDTO.setVideoId(entity.getId());

        categoryVideoService.create(categoryVideoDTO);
    }


    private void createPlaylistVideo(VideoEntity entity, VideoDTO dto) {
        PlaylistVideoDTO playlistVideoDTO = new PlaylistVideoDTO();
        playlistVideoDTO.setVideoId(entity.getId());
        playlistVideoDTO.setPlaylistId(dto.getPlaylistId());
        playlistVideoDTO.setChannelId(entity.getChannelId());
        playlistVideoDTO.setOrder(dto.getOrder());

        playlistVideoService.create(playlistVideoDTO);
    }

    private VideoEntity toEntity(VideoDTO dto) {
        VideoEntity entity = new VideoEntity();
        entity.setAttachId(dto.getAttachId());
        entity.setPreviewId(dto.getPreviewId());
        entity.setChannelId(dto.getChannelId());
        entity.setDescription(dto.getDescription());
        entity.setName(dto.getName());

        return entity;
    }

    public boolean exists(String videoId) {
        return videoRepository.existsById(videoId);
    }

    public int getCount(Integer id) {
        return videoRepository.getCount(id);
    }

    public String update(VideoUpdateDTO dto, String id) {
        Optional<VideoEntity> optional =
                videoRepository.findByIdAndVisible(
                        id, Boolean.TRUE);

        if (optional.isEmpty()) {
            log.error("Video not found!");
            throw new ItemNotFoundException(
                    "Video not found!"
            );
        }

        VideoEntity entity = optional.get();

        if (!profileService.getCurrentUser()
                .getProfile().getId().equals(
                        entity.getChannel().getProfileId())) {
            log.error("Method not allowed!");
            throw new MethodNotAllowedException(
                    "Method not allowed!"
            );
        }

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        videoRepository.update(entity.getName(),
                entity.getDescription(), entity.getId());

        return "Successfully updated a video";

    }

    public String changeStatus(VideoStatusDTO dto, String id) {
        CustomUserDetails user = profileService.getCurrentUser();

        Optional<VideoEntity> optional = videoRepository.
                findByIdAndVisible(id, Boolean.TRUE);

        if (optional.isEmpty()) {
            log.error("Video not found!");
            throw new ItemNotFoundException(
                    "Video not found!"
            );
        }

        VideoEntity entity = optional.get();

        if (!user.getProfile().getId().equals(
                entity.getChannel().getProfileId())) {
            log.error("Method not allowed!");
            throw new ItemNotFoundException(
                    "Method not allowed!"
            );
        }

        entity.setStatus(dto.getStatus());

        videoRepository.changeStatus(entity.getStatus(), entity.getId());

        return "Successfully changed video's status";
    }

    public String increaseViewCount(String id) {
        if (!videoRepository.existsByIdAndVisible(id, Boolean.TRUE)) {
            log.error("Video not found!");
            throw new ItemNotFoundException(
                    "Video not found!"
            );
        }

        videoRepository.increaseViewCount(id);

        return "Successfully increased video's view count";
    }

    public VideoEntity get(String id) {
        return videoRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException("video not found");
        });
    }

    public List<VideoShortInfoDTO> paginationByCategory(Integer categoryId, int size, int page) {
        if (!categoryService.exists(categoryId)) {
            log.error("Category not found!");
            throw new ItemNotFoundException(
                    "Category not found!"
            );
        }

        List<VideoShortInfoRepository> all =
                videoRepository.paginationByCategoryWithShortInfo(
                        categoryId, size, page);

        List<VideoShortInfoDTO> dtoList = new LinkedList<>();

        all.forEach(entity -> {
            VideoShortInfoDTO dto = toDTOWithShortInfo(entity);

            dtoList.add(dto);
        });

        return dtoList;
    }

    private VideoShortInfoDTO toDTOWithShortInfo(VideoShortInfoRepository entity) {
        VideoShortInfoDTO dto = new VideoShortInfoDTO();
        dto.setVideoId(entity.getVideoId());
        dto.setVideoName(entity.getVideoName());
        dto.setVideoPreviewId(entity.getPreviewId());
        dto.setVideoCreatedDate(entity.getCreatedDate());

        dto.setChannelId(entity.getChannelId());
        dto.setChannelPhotoId(entity.getPhotoId());
        dto.setChannelName(entity.getChannelName());

        dto.setViewCount(entity.getViewCount());

        return dto;
    }

    public VideoShortInfoDTO getByName(String name) {
        Optional<VideoShortInfoRepository> optional =
                videoRepository.getByNameWithShortInfo(name);

        if (optional.isEmpty()) {
            log.error("Video not found");
            throw new ItemNotFoundException(
                    "Video not found"
            );
        }

        VideoShortInfoRepository entity = optional.get();

        return toDTOWithShortInfo(entity);
    }

    public VideoFullInfoDTO getById(String videoId) {

        ProfileEntity profile = profileService.getProfile();

        VideoEntity entity = get(videoId);

        if (!entity.getChannel().getProfileId().equals(profile.getId()) &&
                !profile.getRole().equals(ProfileRole.ROLE_ADMIN)) {
            throw new NoPermissionException("no access");
        }

        VideoFullInfoDTO dto = new VideoFullInfoDTO();
        dto.setAttachUrl(attachService.getAttachOpenUrl(entity.getAttachId()));
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPreviewId(entity.getPreviewId());
        dto.setPreviewUrl(attachService.getAttachOpenUrl(entity.getPreviewId()));
        dto.setAttachId(entity.getAttachId());
        dto.setCategoryId(entity.getCategoryId());
        dto.setCategoryName(entity.getCategory().getName());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setChannelId(entity.getChannelId());
        dto.setChannelName(entity.getChannel().getName());
        dto.setChannelUrl(attachService.getAttachOpenUrl(entity.getChannel().getPhotoId()));
        dto.setShareCount(entity.getSharedCount());

        List<VideoTagEntity> list = videoTagRepository.findAllByVideo(entity);
        List<TagDTO> tagDTOList = new ArrayList<>();

        list.forEach(videoTagEntity -> {
            tagDTOList.add(new TagDTO(videoTagEntity.getTagId(),videoTagEntity.getTag().getName()));
        });

        VideoViewLikeDislikeCountAndStatusByProfile count =
                videoWatchedRepository.count(entity.getId(), profile.getId());
        dto.setViewCount(count.getViewCount());
        dto.setLikeCount(count.getLikeCount());
        dto.setDislikeCount(count.getDislikeCount());
        dto.setStatus(LikeStatus.valueOf(count.getStatus()));

        return dto;

    }

}
