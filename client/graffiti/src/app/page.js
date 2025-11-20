import Image from "next/image";
import styles from "./page.module.css";
import MenuComponent from "./component/menu/MenuComponent";
import HeaderComponent from "./component/header/HeaderComponent";
import FeedPreView from "./component/feed/FeedPreView";
export default function Home() {
  return (
    <>
    <HeaderComponent/>
    <div style={{"marginTop":"3rem"}}>
      {
        <FeedPreView/>
      }
    </div>
    <MenuComponent/>
    </>
  );
}
