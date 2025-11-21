import Link from 'next/link';
import style from './MenuComponent.module.css';

export default function MenuComponent() {
  return (
    <div className={style.menuContainer}>
        <div className={style.boarder}>
            <Link href="/feed">
                <div className={style.menuItem}>
                    <img src={'/menu/feed.svg'}></img>
                </div>
            </Link>
            <Link href="/rank">
                <div className={style.menuItem}>
                    <img src={'/menu/rocket.svg'}></img>
                </div>
            </Link>
            <div id={style.add} className={style.menuItem}>
                <img src={'/menu/add.svg'}></img>
            </div>
            <Link href="/group">
                <div className={style.menuItem}>
                    <img src={'/menu/group.svg'}></img>
                </div>
            </Link>
            <Link href="/notification">
                <div className={style.menuItem}>
                    <img src={'/menu/notification.svg'}></img>
                </div>
            </Link>
        </div>
    </div>
  );
}