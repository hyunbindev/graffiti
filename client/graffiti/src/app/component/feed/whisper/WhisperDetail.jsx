"use client";
import style from './WhisperDetail.module.css';
import CommentField from '@/component/comment/CommentField';
import Comment from '@/component/comment/Comment';
export default function WhisperDetail () {
    return(
        <div className={style.detailContainer}>
            <div className={style.header}>
                <div className={style.authorProfile}>
                    <img src='http://k.kakaocdn.net/dn/cbjheL/btsNsQ9q5bf/1uFET5kzv08pRKZLqCgUWk/img_640x640.jpg' />
                    <span>Author Name</span>
                </div>
                <div className={style.createdAt}>
                    10분전
                </div>
            </div>
            <div className={style.content}>
                asdfasdklfjalsd
                asdkl;fjadslkjf
                lkasdjfljasdifsadasd
                asdfasdklfjalsdsdas
                d
                as
                d
                asdfasdklfjalsdsdasdasdassdasdasdassdfasjdfhjkasdf
                asjkdhfkasdhuif
                ajsdhfasdhufiasdfq
                jasdhfaiusdhfuqwe
                asjkdhfiasudhfiuqwe
                kasjdhfuiqewf
            </div>
            <div className={style.info}>
                <div className={style.static}>
                    <img src="/feed/view.svg" alt="view_count" />
                    <span>100</span>
                </div>
                <div className={style.static}>
                    <img src="/feed/like.svg" alt="view_count" />
                    <span>1100</span>
                </div>
                <div className={style.static}>
                    <img src="/feed/comment.svg" alt="view_count" />
                    <span>100</span>
                </div>
                <div className={style.static}>
                    <img src="/feed/delete.svg" alt="view_count" />
                    <span style={{"color":"#EA3323"}}>게시글 삭제</span>
                </div>
            </div>
            <CommentField/>
            <div>
                <Comment/>
                <Comment/>
                <Comment/>
            </div>
        </div>
    );
}