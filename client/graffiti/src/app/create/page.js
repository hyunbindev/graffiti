"use client"
import style from './CreatePage.module.css'
import TextareaAutosize from 'react-textarea-autosize';

import useCreateWhisper from '@/viewmodel/create/useCreateWhisper';
import useImage from '@/viewmodel/create/useImage'

import SearchMember from '@/component/member/search/SearchMember'

export default function CreatePage(){
    const {image, uploadImage ,imageUrl ,removeImage}= useImage();
    const { text ,createFeed ,setText } = useCreateWhisper();
     return (
        <>
        <div className={style.CreatePageContainer}>
            <div className={style.contentContainer}>
                {image && <div className={style.imageContainer}>
                    <img src={imageUrl}/>
                    <button className={style.delete} onClick={removeImage}>
                        <img id={style.delete} src={"/create/delete.svg"}/>
                    </button>
                </div>}
                <TextareaAutosize
                    minRows={3}
                    placeholder="글을 입력해 주세요"
                    value={text}
                    onChange={(e)=>setText(e.target.value)}
                    style={{
                        width: '100%',
                        padding: '0.5rem 3rem 0.5rem 0.5rem',
                        fontSize: '1rem',
                        boxSizing: 'border-box',
                        borderRadius: '5px',
                        outline: 'none',
                        border: 'none'
                    }}
                />
            </div>
            <div className={style.controller}>
                <label className={style.button} htmlFor='file-input'>
                    <img src={"/create/image.svg"}/>
                    사진
                </label>
                <label className={style.button}>
                    <img src={"/create/mention.svg"}/>
                    언급
                </label>
                <label className={style.button} onClick={()=>createFeed(image)}>
                    <img src={"/create/write.svg"}/>
                    게시
                </label>
            </div>
            <input id='file-input' type="file" accept="image/*" style={{"display":"none"}} onChange={uploadImage}/>
        </div>
        <SearchMember/>
        </>
    )
}