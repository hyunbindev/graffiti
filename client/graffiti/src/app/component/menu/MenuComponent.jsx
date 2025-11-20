import style from './MenuComponent.module.css';

export default function MenuComponent() {
  return (
    <div className={style.menuContainer}>
        <div className={style.boarder}>
            <div className={style.menuItem}>
                <img src={'/menu/feed.svg'}></img>
            </div>
            <div id={style.add} className={style.menuItem}>
                <img src={'/menu/add.svg'}></img>
            </div>
            <div className={style.menuItem}>
                <img src={'/menu/notification.svg'}></img>
            </div>
        </div>
    </div>
  );
}