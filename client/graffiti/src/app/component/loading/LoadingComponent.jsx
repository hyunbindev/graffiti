import CircularProgress from '@mui/material/CircularProgress';
import style from './LoadingComponent.module.css'
export default function LoadingComponent({ children }) {
  return (
    <div className={style.loadingContainer}>
      <CircularProgress id={style.animation} size={"5rem"} sx={{ color: '#00c3ffff' }}/>
      <div id={style.message}>{children}</div>
      <div id={style.comment}>잠시만 기다려 주세요.</div>
    </div>
  );
}