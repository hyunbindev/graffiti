"use client";
import style from './HeaderComponent.module.css'
import { useAuthStore } from '../../zustand/useAuthStore.js';
import { FormControl, InputLabel, Select, MenuItem } from '@mui/material';
export default function HeaderComponent() {
    const { nickName, profileImgeUrl,groups ,selectedGroup} = useAuthStore();
    return (
        <div className={style.headerContainer}>
        <FormControl
        size="small"
        sx={{
            "& .MuiInputBase-root": {
            height: 28,
            fontSize: '1rem',
            borderRadius: 1,
            },
            "& .MuiSelect-select": {
            paddingY: 0.3,        // 패딩 최소
            paddingX: 1,
            },
        }}
        >
        <Select value={selectedGroup?.uuid ?? ""}>
            {
                groups.map((value)=>(<MenuItem key={value.uuid} value={value.uuid}>{value.name}</MenuItem>))
            }
        </Select>
        </FormControl>
            <div id={style.profile}>
                <img src={profileImgeUrl} alt="profile" />
                <span>{nickName}</span>
            </div>
        </div>
    );
}