import style from './Comment.module.css'
import {getTimeConvert} from '@/util/dateConvert'
import { useAuthStore } from '@/zustand/useAuthStore';
import {deleteCommentFeeds} from '@/model/CommentModel'
export default function Comment({comment , refreshComment}){
    const { uuid } = useAuthStore();

const deleteComment = async()=>{
        // 1. 작성자와 현재 사용자가 일치하는지 다시 확인 (방어적 코드)
        if (uuid !== comment.author.uuid) return;
        
        // 2. 사용자에게 삭제 여부 확인
        const isConfirmed = confirm("댓글을 삭제하시겠습니까?");

        if (!isConfirmed) {
            return; // 사용자가 취소하면 함수 종료
        }

        try {
            // 3. API 호출하여 댓글 삭제
            await deleteCommentFeeds(comment.id);
            refreshComment();
            
        } catch (error) {
            console.error("댓글 삭제 실패:", error);
            alert("댓글 삭제에 실패했습니다.");
        }
    }

    return (
    <div className={style.commentContainer}>
        { uuid == comment.author.uuid && <div className={style.delete} onClick={deleteComment}>
            <img src="/feed/delete.svg" alt="view_count" />
            <span style={{"color":"#EA3323"}}>삭제</span>
        </div>}
        <div className={style.header}>
            <div className={style.authorProfile}>
                <img src={comment.author.profileImg} alt="profile" className={style.profileImg}/>
                <span>{comment.author.nickName}</span>
            </div>
            <div className={style.createdAt}>{getTimeConvert(comment.createdAt)}</div>
        </div>
        <div className={style.text} style={{"whiteSpace":"pre-wrap"}}>
            {comment.text} 
        </div>
    </div>);
}