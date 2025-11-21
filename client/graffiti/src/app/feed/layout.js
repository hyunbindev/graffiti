import MenuComponent from "@/component/menu/MenuComponent";
import HeaderComponent from "@/component/header/HeaderComponent";
export default function FeedLayout({ children }) {
  // children: feed/page.js (게시글 목록)
  // detail: @detail/[feed_id]/page.js (모달/상세)

  return (
    <>
        <HeaderComponent/>
        {children}
        <MenuComponent/>
    </>
  );
}