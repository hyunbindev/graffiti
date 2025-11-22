import Box from '@mui/material/Box';
import Modal from '@mui/material/Modal';
import * as React from 'react';

import useGroupInvite from '@/viewmodel/group/useGroupInvite';


import { useEffect } from 'react';


import style from './GroupInviteModal.module.css'

const modalStyle = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 400,
  bgcolor: 'background.paper',
  boxShadow: 24,
  p: 3,
  borderRadius: '5px',
  outline: 'none',
};

export default function GroupInviteModal({selectedGroup,setSelectedGroup}){
    const isOpen = true; 
    const handleClose = () => {
        setSelectedGroup(null);
    };
    const {inviteCode, getCode} = useGroupInvite();

    useEffect(()=>{
        console.log(selectedGroup)
        getCode(selectedGroup.uuid);
        return;
    },[selectedGroup]);
    const handleCopy = async () => {
        if (!inviteCode) {
            alert("초대 코드가 발급되지 않았습니다.");
            return;
        }
        try {
            // 클립보드 API를 사용하여 텍스트 복사
            await navigator.clipboard.writeText(inviteCode);
            alert("초대 코드가 클립보드에 복사되었습니다!");
        } catch (err) {
            console.error('클립보드 복사 실패:', err);
            alert("클립보드 복사에 실패했습니다. 브라우저 설정을 확인해 주세요.");
        }
    };
    return(
        <Modal
            open={isOpen}
            onClose={handleClose}
            aria-labelledby="modal-title"
            aria-describedby="modal-description">
            <Box sx={modalStyle}>
                <h3 className={style.title}>초대코드 발급</h3>
                {inviteCode ? <p>{selectedGroup.name}의 초대코드가 발급되었습니다.</p> : <p>{selectedGroup.name}의 초대코드 발급 중.</p>}
                <div className={style.code}>
                    {inviteCode}
                </div>
                <div className={style.buttonGroup}>
                    <button onClick={handleClose}>닫기</button>
                    <button onClick={handleCopy} disabled={!inviteCode}>코드 복사</button>
                    <button disabled={!inviteCode}>공유</button>
                </div>
            </Box>
        </Modal>
    )
}