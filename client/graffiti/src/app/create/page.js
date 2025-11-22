"use client"
import style from './CreatePage.module.css'
import TextareaAutosize from 'react-textarea-autosize';

import useCreateWhisper from '@/viewmodel/create/useCreateWhisper';
import useImage from '@/viewmodel/create/useImage'

import SearchMember from '@/component/member/search/SearchMember'

import {useState,useEffect} from 'react';
import FormControlLabel from '@mui/material/FormControlLabel';
import MemberElement from '@/component/member/search/MemberElement';
import Checkbox from '@mui/material/Checkbox';
export default function CreatePage(){
    const {image, uploadImage ,imageUrl ,removeImage}= useImage();
    const { text ,createFeed ,setText } = useCreateWhisper();
    const [mentions , setMentions] = useState([]);
    const [visibleModal, setVisibleModal] = useState(false);
    const [invisibleMention, setinvisibleMention] = useState(false);
    const mentionAdd = (member) => {
        setMentions((prev) => {
            const isDuplicate = prev.some(existingMember => existingMember.uuid === member.uuid);
            if (isDuplicate) {
                return prev;
            }
            return [...prev, member];
        });
    };

    const closeModal = ()=>{
        setVisibleModal(false);
    }

    const deleteMention = (member) => {
        setMentions((prev) => {
            return prev.filter(existingMember => existingMember.uuid !== member.uuid);
        });
    };

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
            {mentions.length>0 &&
            <div className={style.mentionsContainer}>
                <div className={style.list}>
                {
                    mentions.map((m,i)=>(<MemberElement key={m.uuid} member={m} clickHandler={deleteMention}/>))
                }
                </div>
                <FormControlLabel
                control={
                    <Checkbox 
                    checked={invisibleMention}
                    onChange={(e)=>{setinvisibleMention(e.target.checked)}}
                    />
                }
                label="언급 사용자 비공개" 
                />
            </div>}
            <div className={style.controller}>
                <label className={style.button} htmlFor='file-input'>
                    <img src={"/create/image.svg"}/>
                    사진
                </label>
                <label className={style.button} onClick={()=>setVisibleModal(true)}>
                    <img src={"/create/mention.svg"}/>
                    언급
                </label>
                <label className={style.button} onClick={()=>createFeed(image,mentions,invisibleMention)}>
                    <img src={"/create/write.svg"}/>
                    게시
                </label>
            </div>
            <input id='file-input' type="file" accept="image/*" style={{"display":"none"}} onChange={uploadImage}/>
        </div>
        {visibleModal && <SearchMember addHandler={mentionAdd} onClose={closeModal}/>}
        </>
    )
}