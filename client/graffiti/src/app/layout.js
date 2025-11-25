import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";
import KakaoScript from "./share/KakaoScript";
import Redirect from '@/lib/Redirect'
const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata = {
  title: "Graffiti",
  description: "Welcome to group SNS graffiti",
};

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body className={`${geistSans.variable} ${geistMono.variable}`}>
        {children}
      </body>
      <KakaoScript/>
      <Redirect/>
    </html>
  );
}