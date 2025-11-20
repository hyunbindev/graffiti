"use client";
import style from './HeaderComponent.module.css'
import { useAuthStore } from '../../zustand/useAuthStore.js';
export default function HeaderComponent() {
    const { nickName, profileImgeUrl } = useAuthStore();
    return (
        <div className={style.headerContainer}>
            <div>그룹 선택</div>
            <div id={style.profile}>
                <img src={profileImgeUrl} alt="profile" />
                <span>{nickName}</span>
            </div>
        </div>
    );
}