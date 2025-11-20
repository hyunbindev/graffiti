"use client";
import style from './WhisperPreview.module.css'

export default function WhisperPreview({whisper}){


    return (
        <div className={style.whisperPreviewContainer}>
            {whisper.imageUrl && <img  src={whisper.imageUrl} alt="image"/>}
            <div>{whisper.previewText}</div>
            {whisper.mentionMember.length > 0 &&<div className={style.mentionContainer}>
            {
                whisper.mentionMember.map((member)=>(
                    <div key={member.uuid} className={style.mentionMember}>
                        <img src={member.profileImg} alt="profile"/>
                        <span>{member.nickName}</span>
                    </div>
                ))
            }
            </div>}
        </div>
    );
}