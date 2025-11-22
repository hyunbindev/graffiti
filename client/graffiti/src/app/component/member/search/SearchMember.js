import Box from '@mui/material/Box';
import Modal from '@mui/material/Modal';
import * as React from 'react';
import TextField from '@mui/material/TextField';

import useGroupInvite from '@/viewmodel/useGroupInvite';

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

export default function SearchMember(){
    return(
        <Modal
            open={true}
            onClose={()=>{}}
            aria-labelledby="modal-title"
            aria-describedby="modal-description">
            <Box sx={modalStyle}>
                <TextField id="standard-basic" label="동대문 킬러에서 멤버 검색" variant="standard" />
            </Box>
        </Modal>
    );
}