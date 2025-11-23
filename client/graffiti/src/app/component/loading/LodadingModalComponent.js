'use client';

import * as React from 'react';
import { Modal, Box } from '@mui/material';

import CircularProgress from '@mui/material/CircularProgress';

import style from './LoadingModalComponent.module.css'
const modalStyle = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  minWidth: 300,
  maxHeight: '90vh', // 최대 높이 지정
  overflowY: 'auto', // 내용이 넘칠 경우 스크롤 허용
  bgcolor: 'background.paper', // 흰색 배경
  borderRadius: 2, // 모서리 둥글게
  boxShadow: 24,
  p: 4, 
  outline: 'none', // 포커스 시 외곽선 제거
};

/**
 * 재사용 가능한 MUI 모달 컴포넌트
 * @param {boolean} open - 모달 열림/닫힘 상태
 * @param {function} onClose - 모달 닫기 핸들러
 * @param {React.ReactNode} children - 모달 내부에 렌더링할 내용
 */
export default function LoadingModal({ open, onClose, children }) {
  return (
    <Modal
      open={open}
      onClose={onClose}
      aria-labelledby="custom-modal-title"
      aria-describedby="custom-modal-description"
    >
      <Box sx={modalStyle}>
        <div className={style.content}>
            <div style={{marginBottom:"1rem"}}><CircularProgress /></div>
            <div style={style.text}>{children}</div>
        </div>
      </Box>
    </Modal>
  );
}