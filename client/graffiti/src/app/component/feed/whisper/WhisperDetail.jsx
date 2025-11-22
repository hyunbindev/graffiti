"use client";
import style from './WhisperDetail.module.css';
import CommentField from '@/component/comment/CommentField';
import Comment from '@/component/comment/Comment';
import {getTimeConvert} from '@/util/dateConvert';
import { useAuthStore } from '@/zustand/useAuthStore';
import { Skeleton } from '@mui/material';
import { useState } from 'react';
import useFeedComment from '@/viewmodel/useGetComment';
import {useEffect, useCallback} from 'react';
import { useInView } from 'react-intersection-observer';
import {deleteWhisperFeed} from '@/model/FeedModel'
import useLike from '@/viewmodel/useLike'
export default function WhisperDetail ({feed}) {
    const { uuid } = useAuthStore();
    const [imageLoaded, setImageLoaded] = useState(false);
    const {postLike, likeCount ,isLiked} = useLike(feed.id, feed.likeCount, feed.isLiked);
    const {comments, getNextPage, hasMore, text, setText, postgetComment, refreshComment} = useFeedComment(feed.id); 
    
    const [ref, inView] = useInView();
    
const deleteFeed= async()=>{
        // 1. 본인 게시글이 아니면 바로 리턴
        if(feed.author.uuid !== uuid) return;
        
        // 2. 사용자에게 삭제 확인 받기
        const isConfirmed = confirm("정말로 게시글을 삭제하시겠습니까?");

        if (!isConfirmed) {
            return; // 사용자가 취소하면 중단
        }

        try{
            // 3. API 호출
            await deleteWhisperFeed(feed.id);
            
            alert("게시글이 삭제되었습니다.");
            
            window.location.href = '/feed'

        }catch(e){
            console.error("게시글 삭제 중 오류 발생:", e);
            alert("게시글 삭제에 실패했습니다.");
        }
    }

    useEffect(()=>{
        if (inView && hasMore) {
            getNextPage();
        }
    },[inView, hasMore, getNextPage]); 
    
    return(
        <>
        <div className={style.detailContainer}>
            <div className={style.header}>
                <div className={style.authorProfile}>
                    <img src={feed?.author.profileImg} />
                    <span>{feed?.author.nickName}</span>
                </div>
                <div className={style.createdAt}>
                    {getTimeConvert(feed?.createdAt)}
                </div>
            </div>
            {feed?.imageUrl &&
                <div className={style.imageContainer}>
                    <img className={style.whisperImage} src={feed.imageUrl} style={{"display": imageLoaded ? 'block' : 'none'}} onLoad={() => setImageLoaded(true)}/>
                    {!imageLoaded && <Skeleton 
                        variant="rectangular"
                        className={style.whisperImage}
                        sx={{ borderRadius:'5px', width: '100%', height: '16rem' }}
                    />}
                </div>
            }
            <div className={style.content} style={{"whiteSpace":"pre"}}>
                {feed && feed?.text}
            </div>
            {feed.metionMembers.length > 0 &&<div className={style.mentionContainer}>
                {
                feed.metionMembers.map((member)=>(
                    <div key={member.uuid} className={style.mentionMember}>
                        <img src={member.profileImg} alt="profile"/>
                        <span>{member.nickName}</span>
                    </div>
                ))
                }
            </div>}
            <div className={style.info}>
                <div className={style.static}>
                    <img src="/feed/view.svg" alt="view_count" />
                    <span>{feed?.viewCount}</span>
                </div>
                <div className={style.static} onClick={postLike}>
                    {isLiked==false && <img src="/feed/like.svg" alt="view_count" />}
                    {isLiked==true && <img src="/feed/unlike.svg" alt="view_count" />}
                    <span>{likeCount}</span>
                </div>
                <div className={style.static}>
                    <img src="/feed/comment.svg" alt="view_count" />
                    <span>{feed?.commentCount}</span>
                </div>
                {
                    //작성자와 사용자 UUID 비교 후 일치하면 삭제 아이콘 표시
                    feed?.author.uuid == uuid &&
                    <div className={style.static} onClick={deleteFeed}>
                        <img src="/feed/delete.svg" alt="view_count" />
                        <span style={{"color":"#EA3323"}}>게시글 삭제</span>
                    </div>
                }
            </div>
            <CommentField text={text} setText={setText} submitHandler={postgetComment}/>
            <div>
                {
                    comments.map((c)=>(
                        <Comment key={c.id} comment={c} refreshComment={refreshComment}/>
                    ))
                }
            </div>

        </div>
        {/* 무한 스크롤 감지 요소 */}
        <div ref={ref} style={{"height":"5rem", "backgroundColor":"transparent"}}>
            {hasMore && <p style={{textAlign: 'center', color: '#999'}}>댓글 불러오는 중...</p>} 
        </div>
        </>
    );
}