"use client";
import Link from 'next/link';
import style from './WhisperPreview.module.css'

export default function WhisperPreview({whisper}){

    const preViewText=(text)=>{
        if(text.length>=49){
            return text+".... 더보기"
        }
        return text;
    }

    return (
        <Link href={`feed/${whisper.id}`} className={style.whisperLink}> 
        <div className={style.whisperPreviewContainer}>
            {whisper.imageUrl && <img  src={whisper.imageUrl} alt="image"/>}
            <div style={{"whiteSpace":"pre-wrap"}}>{preViewText(whisper.previewText)}</div>
            {whisper.mentionMember.length > 0 &&<div className={style.mentionContainer}>
            {
                whisper.mentionMember.map((member)=>(
                    <div key={member.uuid} className={style.mentionMember}>
                        <img src={"/feed/tag.svg"} alt="tag_icon"/>
                        <img src={member.profileImg} alt="profile"/>
                        <span>{member.nickName}</span>
                    </div>
                ))
            }
            </div>}
        </div>
        </Link>
    );
}