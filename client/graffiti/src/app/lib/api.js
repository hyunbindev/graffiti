import axios from 'axios';
import { useAuthStore } from '../zustand/useAuthStore';
const api = axios.create({
    withCredentials: true,
});

api.interceptors.request.use(
    (config) => {
        const token = useAuthStore.getState().accessToken;
        if(token){
            config.headers['Authorization'] = `${token}`;
        }
        return config;
    },
    (error)=> Promise.reject(error)
);
export default api;