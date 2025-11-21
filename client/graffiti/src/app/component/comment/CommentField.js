import TextareaAutosize from 'react-textarea-autosize';
import style from './CommentField.module.css';
export default function CommentField({text,setText,submitHandler}) {
    return (<div style={{"marginTop":"0.5rem","position":"relative"}}>
    <TextareaAutosize
      minRows={3} // 최소 높이를 3줄로 설정
      maxRows={10}
      value={text}
      onChange={(e)=>setText(e.target.value)}
      placeholder="덧글을 입력해 주세요."
      style={{
        width: '100%',
        padding: '0.5rem 3rem 0.5rem 0.5rem',
        fontSize: '1rem',
        boxSizing: 'border-box',
        borderRadius: '5px',
        outline: 'none',
        border: '1px solid #ccc',
      }}
    />
    <button className={style.send} onClick={submitHandler}>
        <img src="/comment/send.svg" alt="send"/>
    </button>
    </div>);
}