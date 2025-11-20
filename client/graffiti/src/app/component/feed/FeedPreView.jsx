"use client";
import { useAuthStore } from '../../zustand/useAuthStore.js';
import style from './FeedPreView.module.css'
export default function FeedPreView() {
    const { nickName, profileImgeUrl } = useAuthStore();
    return (
        <div className={style.feedPreviewContainer}>
            <div className={style.feedPreview}>
                <div className={style.feedPreviewHeader}>
                    <div className={style.authorProfile}>
                        <img src='http://k.kakaocdn.net/dn/cbjheL/btsNsQ9q5bf/1uFET5kzv08pRKZLqCgUWk/img_640x640.jpg'/>
                        <span>김현빈</span>
                    </div>
                    <div className={style.createdAt}>
                        30분 전
                    </div>
                </div>
                <div className={style.content}>content</div>
                <div className={style.info}>
                    <div className={style.static}>
                        <img src="/feed/view.svg" alt="view_count" />
                        <span>1,569</span>
                    </div>
                    <div className={style.static}>
                        <img src="/feed/like.svg" alt="view_count" />
                        <span>1,569</span>
                    </div>
                    <div className={style.static}>
                        <img src="/feed/comment.svg" alt="view_count" />
                        <span>1,569</span>
                    </div>
                </div>
            </div>
        </div>
    );
}