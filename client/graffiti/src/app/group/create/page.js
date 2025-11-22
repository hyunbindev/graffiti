"use client";
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import style from './GroupCreatePage.module.css'
import useCreateGroup from '@/viewmodel/group/useCreateGroup';

export default function GroupCreatePage(){

    const  { groupName, setGroupName , createGroup } = useCreateGroup();

    return(
        <div className={style.GroupCreatePage}>
            <img src={"/group/groupgreeting.svg"}/>
            <h2 style={{"marginBottom":"1rem"}}>새로운 그룹</h2>
            <TextField 
            color="success"
            id="minimal-input" 
            label="그룹 이름" 
            variant="standard"
            value={groupName}
            onChange={(e)=>setGroupName(e.target.value)}
            />
            <Button id={style.createBtn} variant="contained" color="success" onClick={createGroup}>
                그룹생성
            </Button>
        </div>
    )
}