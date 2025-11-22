import Skeleton from "@mui/material/Skeleton";

import style from './BlinedFeed.module.css'

export default function BlinedFeed() {
    return (
        <div className={style.feedPreviewContainer}>
            <div className={style.feedPreview}>
                <div className={style.feedPreviewHeader}>
                    <div className={style.authorProfile}>
                        <Skeleton variant="circular" width={32} height={32} />
                        <Skeleton variant="text" width={80} height={20} />
                    </div>
                    <div className={style.createdAt}>
                        <Skeleton variant="text" width={50} height={18} />
                    </div>
                </div>

                {/* Content */}
                <div className={style.content}>
                    <Skeleton variant="rectangular" width="100%" height={16} />
                    <Skeleton variant="rectangular" width="100%" height={16} sx={{ mt: 1 }} />
                    <Skeleton variant="rectangular" width="80%" height={16} sx={{ mt: 1 }} />
                </div>
            </div>
            <div className={style.blindMessage}>
                비공개 게시글 입니다.
            </div>
        </div>
    );
}
