import Image from "next/image";
import styles from "./page.module.css";

export default function Home() {
  return (
    <div>
      <h1 className={styles.title}>Welcome to Graffiti!</h1>
    </div>
  );
}
