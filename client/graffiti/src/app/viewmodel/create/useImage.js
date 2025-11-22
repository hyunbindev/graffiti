"user client";

import { useEffect, useState } from "react";

const useImage = () => {
    const [image, setImage] = useState(null);
    const [imageUrl, setImageUrl] = useState(null);
    const uploadImage = (event)=>{
        if (event.target.files && event.target.files.length > 0) {
            setImage(event.target.files[0]); 
        }
    }
    const removeImage = () => {
        setImage(null);
    }
    useEffect(() => {
        if (!image) {
            setImageUrl(null);
            return;
        }

        // 파일 객체로부터 임시 URL 생성
        const objectUrl = URL.createObjectURL(image);
        setImageUrl(objectUrl);

        // 컴포넌트 언마운트 또는 file이 변경될 때 이전 URL 해제 (메모리 누수 방지)
        return () => URL.revokeObjectURL(objectUrl);
    }, [image]);


    return { image , uploadImage ,imageUrl ,removeImage};
}
export default useImage;