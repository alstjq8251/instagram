package com.sparta.instagram.service;


import com.sparta.instagram.domain.*;
import com.sparta.instagram.domain.dto.MemberSearchCondition;
import com.sparta.instagram.domain.dto.requestdto.ArticleRequestDto;
import com.sparta.instagram.domain.dto.responsedto.ArticleResponseDto;
import com.sparta.instagram.domain.dto.S3Dto;
import com.sparta.instagram.domain.dto.Time;
import com.sparta.instagram.domain.dto.responsedto.HeartResponseDto;
import com.sparta.instagram.domain.dto.responsedto.MemberDto;
import com.sparta.instagram.repository.ArticleRepository;
import com.sparta.instagram.repository.DeletedUrlPathRepository;
//import com.sparta.instagram.repository.HeartRepository;
import com.sparta.instagram.repository.ImageRepository;
import com.sparta.instagram.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ArticleService {

//    private final HeartRepository heartRepository;
    private final DeletedUrlPathRepository deletedUrlPathRepository;
    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;
    private final ArticleRepository articleRepository;

    private final MemberRepository memberRepository;

    //게시글 사진,내용 등록
    @Transactional
    public ArticleResponseDto CreateArticle(ArticleRequestDto requestDto, List<MultipartFile> multipartFile, UserDetails userDetails) throws IOException {
        if(multipartFile.isEmpty()){
            throw new IllegalArgumentException("게시글 작성시 이미지 파일이 필요합니다.");
        }
        if(requestDto.getContent() == null){
            throw new IllegalArgumentException("게시글 작성시 내용을 작성하여 등록해주세요");
        }

        String info[] = userDetails.getUsername().split(" ");
        List<Image> imageList = new ArrayList<>();
        Map<String, Boolean> map = new HashMap() {{  // 용량을 동적으로 늘리는 Map을 만들어라 -> 순서가 유지안됨 iterator 넣은순서가유지가안됨!
            put(info[0], false);
        }};
        Article article = Article.builder()
                .content(requestDto.getContent())
                .userId(info[0])
                .userNic(info[1])
                .userName(info[2])
                .userLike(false)
                .heartmap(map)
                .build();
        for(MultipartFile multipartFile1 : multipartFile){
            S3Dto s3Dto = s3Uploader.upload(multipartFile1);
            Image image = Image.builder()
                    .article(article)
                    .ImgUrl(s3Dto.getUploadImageUrl())
                    .ImgName(s3Dto.getFileName())
                    .build();
            imageList.add(imageRepository.save(image));
        }
        List<String> imgUrl = imageList
                .stream()
                .map(Image::getImgUrl)
                .collect(Collectors.toList());
        articleRepository.save(article);
        return ArticleResponseDto.builder()
                .userFlag(true)
                .createdAt(article.getCreatedAt())
                .modifiedAt(article.getModifiedAt())
                .userLike(article.getHeartmap().get(info[0]))
                .ImgUrl(imgUrl)
                .id(article.getId())
                .TimeMsg(Time.calculateTime(article.getCreatedAt()))
                .userId(article.getUserId())
                .userNic(article.getUserNic())
                .userName(article.getUserName())
                .content(article.getContent())
                .build();
    }

    //게시글 전체 조회
    public List<ArticleResponseDto> readArticle(UserDetails userDetails) {
        String info[] = userDetails.getUsername().split(" ");
        boolean userFlag = false;
        long count = 0;
        List<Article> articleList = articleRepository.findAllByOrderByModifiedAtDesc();
        List<ArticleResponseDto> articleResponseDtoList = new ArrayList<>();
        for(Article article : articleList){
            if(info[0].equals(article.getUserId())){
                userFlag = true;
            }else userFlag = false;
            Set<Entry<String,Boolean>> entryset1 = article.getHeartmap().entrySet();
            for(Entry<String,Boolean> entryset2 : entryset1){
                if(entryset2.getValue() && entryset2.getKey().equals(info[0]))
                    count++;
            }
//            if(heartRepository.existsByUserIdAndArticle(info[0],article)){
//                article.setUserLike(true);
//            }else {article.setUserLike(false);}
            List<String> imgUrl = imageRepository.findAllByArticle(article)
                    .stream()
                    .map(Image::getImgUrl)
                    .collect(Collectors.toList());
            articleResponseDtoList.add(ArticleResponseDto.builder()
                    .id(article.getId())
                    .userId(article.getUserId())
                    .content(article.getContent())
                    .userNic(article.getUserNic())
                    .userName(article.getUserName())
                    .createdAt(article.getCreatedAt())
                    .modifiedAt(article.getModifiedAt())
                    .heartcnt(count)
//                    .heartcnt(article.getHeartList().size())
                    .imageList(article.getImageList())
                    .commentcnt(article.getCommentList().size())
                    .userLike(article.getHeartmap().get(info[0]))
                    .userFlag(userFlag)
                    .TimeMsg(Time.calculateTime(article.getCreatedAt()))
                    .ImgUrl(imgUrl)
                    .build());
        }
        return articleResponseDtoList;
    }

    //게시글 수정
    @Transactional
    public Article fixArticle(Long id, ArticleRequestDto articleRequestDto, UserDetails userDetails) {
        String core[] = userDetails.getUsername().split(" ");
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        if(!core[0].equals(article.getUserId())){
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }
        if(articleRequestDto.getContent() == null){
            articleRequestDto.setContent(article.getContent());
        }
        article.update(articleRequestDto);
        return article;
    }


    //게시글 삭제
    public boolean deleteArticle(Long id, UserDetails userDetails) {
        String core[] = userDetails.getUsername().split(" ");
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        if(!core[0].equals(article.getUserId())){
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }
        List<Image> imageList = imageRepository.findAllByArticle(article);
        for(Image image : imageList){
            DeletedUrlPath deletedUrlPath =DeletedUrlPath.builder()
                    .deletedUrlPath(image.getImgUrl())
                    .build();
            deletedUrlPathRepository.save(deletedUrlPath);
            article.deleteImage(image);

        }
        articleRepository.delete(article);
        return true;
    }

    public void removeS3Image() {
        List<DeletedUrlPath> deletedUrlPaths = deletedUrlPathRepository.findAll();
        for(DeletedUrlPath deletedUrlPath : deletedUrlPaths){
            s3Uploader.remove(deletedUrlPath.getDeletedUrlPath());
        }
        deletedUrlPathRepository.deleteAll();
    }

    @Transactional
    public HeartResponseDto addArticleHeart(Long id, UserDetails userDetails) {
        String info[] = userDetails.getUsername().split(" ");
        long count=0;
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        article.fixHeartMap(info[0], !article.getHeartmap().get(info[0]));
        Set<Entry<String,Boolean>> entryset1 = article.getHeartmap().entrySet();
        for(Entry<String,Boolean> entryset2 : entryset1){
            if(entryset2.getValue() && entryset2.getKey().equals(info[0]))
                count++;
        }
        return HeartResponseDto.builder()
                .likeFlag(article.getHeartmap().get(info[0]))
                .likeCount(count)
                .build();
    }



}

