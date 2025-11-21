import MenuComponent from "@/component/menu/MenuComponent";
import HeaderComponent from "@/component/header/HeaderComponent";
export default function FeedLayout({ children }) {
  return (
    <>
        {children}
        <MenuComponent/>
    </>
  );
}