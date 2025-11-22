import Box from '@mui/material/Box';
import Modal from '@mui/material/Modal';
import TextField from '@mui/material/TextField';
import { useAuthStore } from '@/zustand/useAuthStore.js';

import {useState, useEffect} from 'react';
import {getMemberInGroup} from '@/model/GroupModel'
import MemberElement from './MemberElement.js';

const modalStyle = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 400,
    bgcolor: 'background.paper',
    boxShadow: 24,
    p: 2,
    borderRadius: '5px',
    outline: 'none',
};

export default function SearchMember({addHandler,onClose}){
    const { selectedGroup} = useAuthStore();
    const [members,setMembers] = useState([]);

    const fetchMembers = async (keyWord) => {
        try {
            const res = await getMemberInGroup(selectedGroup.uuid, keyWord);
            setMembers(res);
        } catch (err) {
            console.error(err);
        }
    };

    useEffect(()=>{
        fetchMembers(null);
    },[]);

    return(
        <Modal
            open={true}
            onClose={onClose}
            aria-labelledby="modal-title"
            aria-describedby="modal-description">
            <Box sx={modalStyle}>
                <TextField id="standard-basic" label={`${selectedGroup.name}에서 멤버 검색`} variant="standard" style={{"width":"100%"}}/>
                <div>
                    {
                        members.map((m,i)=>(<MemberElement key={i} member ={m} clickHandler={addHandler}/>))
                    }
                </div>
            </Box>
        </Modal>
    );
}