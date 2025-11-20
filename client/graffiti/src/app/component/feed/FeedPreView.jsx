"use client";
import { useAuthStore } from '../../zustand/useAuthStore.js';
import style from './FeedPreView.module.css'
import WhisperPreview from '../feed/whisper/WhisperPreview.jsx';
import {getTimeConvert} from '../../util/dateConvert.js';
export default function FeedPreView({data}) {
    const { nickName, profileImgeUrl } = useAuthStore();

    const renderContent = (feed)=>{
        switch(feed.type){
            case "WHISPER":
                return <WhisperPreview whisper={feed}/>;
            case "SECRET":
                return <p>secret</p>
        }
    }

    return (
        <div className={style.feedPreviewContainer}>
            <div className={style.feedPreview}>
                <div className={style.feedPreviewHeader}>
                    <div className={style.authorProfile}>
                        <img src={data.authorInfo.profileImg}/>
                        <span>{data.authorInfo.nickName}</span>
                    </div>
                    <div className={style.createdAt}>
                        {getTimeConvert(data.createdAt)}
                    </div>
                </div>
                <div className={style.content}>
                    {renderContent(data)}
                </div>
                <div className={style.info}>
                    <div className={style.static}>
                        <img src="/feed/view.svg" alt="view_count" />
                        <span>{data.viewCount}</span>
                    </div>
                    <div className={style.static}>
                        <img src="/feed/like.svg" alt="view_count" />
                        <span>{data.likeCount}</span>
                    </div>
                    <div className={style.static}>
                        <img src="/feed/comment.svg" alt="view_count" />
                        <span>{data.commentCount}</span>
                    </div>
                </div>
            </div>
        </div>
    );
}