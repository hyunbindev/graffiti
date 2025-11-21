"use client";
import { getCommentFeeds ,postCommentFeeds } from "@/model/CommentModel";
import { useEffect, useState, useCallback } from "react";

/**
 * 커서 기반으로 특정 게시글의 댓글을 가져오는 커스텀 훅
 *
 * 이 훅은 getCommentFeeds API가 다음 페이지의 데이터를 중복 없이,
 * 기존 목록의 순서를 이어받아 제공한다고 가정하고 구현되었습니다.
 *
 * @param {number} feedId - 댓글을 가져올 게시글의 ID
 * @param {number} size - 페이지당 가져올 댓글 수
 * @returns {{comments: Array, getNextPage: Function, hasMore: boolean}}
 */
const useFeedComment = (feedId, size = 10) => {
    const [comments, setComments] = useState([]);
    const [lastId, setLastId] = useState(null);
    const [hasMore, setHasMore] = useState(true); // 더 불러올 데이터가 있는지 여부
    const [text,setText] = useState("");

    const getComments = useCallback(async () => {
        // 더 이상 데이터가 없으면 API 호출을 막습니다.
        if (!hasMore && lastId !== null) return;
        
        try {
            const data = await getCommentFeeds(feedId, lastId, size);
            
            // 1. 새로운 데이터를 기존 목록에 추가하며 **중복 제거** 로직 적용
            setComments(prevComments => {
                // 기존 댓글과 새로 불러온 댓글을 합칩니다.
                const combinedComments = [...prevComments, ...data];
                
                // Map 객체를 사용하여 ID를 기준으로 중복을 제거합니다.
                const commentMap = new Map();
                combinedComments.forEach(comment => {
                    // 같은 ID를 가진 댓글이 있다면 덮어쓰거나 (API 응답 순서에 따라)
                    // 아니면 새로운 댓글을 추가하여 최종적으로 고유한 댓글만 남깁니다.
                    commentMap.set(comment.id, comment);
                });

                // Map의 값을 배열로 변환하여 반환합니다.
                return Array.from(commentMap.values());
            });
            
            // 2. 커서(lastId) 추출 및 업데이트
            if (data.length > 0) {
                const newLastId = data[data.length - 1].id; 
                setLastId(newLastId);
            }

            // 3. 더 불러올 데이터가 있는지 확인
            if (data.length < size) {
                setHasMore(false);
            }
            
        } catch (error) {
            console.error(error);
        }
    }, [feedId, lastId, size, hasMore]); 

    // 컴포넌트 마운트 시 초기 댓글을 가져옵니다.
    useEffect(() => {
        if (lastId === null) {
             getComments();
        }
    }, [getComments, lastId]);

    // 외부 컴포넌트에서 호출할 "다음 페이지 불러오기" 함수
    const getNextPage = useCallback(() => {
        getComments();
    }, [getComments]);

    const postgetComment =async()=>{
        try{
            await postCommentFeeds(feedId,text);
            setText("");
            refreshComment();
        }catch(e){
            console.error(e);
        }
    }
    const refreshComment = ()=>{
            setLastId(null);
            setComments([]);
            getComments();
            setHasMore(true);
    }
    return { comments, getNextPage, hasMore ,text ,setText,postgetComment ,refreshComment};
}

export default useFeedComment;