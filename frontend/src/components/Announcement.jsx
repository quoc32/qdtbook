
import styles from "./Announcement.module.css"

const Announcement = ({ text, successfull = true, duration = 3000 }) => {
  return (
    <div
      className={`animate__animated animate__bounceInRight animate__faster 
      ${styles["Announcement-container"]} 
      ${styles[`Announcement-container-${successfull ? "success" : "error"}`]}`}
    >
      {text}
      <div
        className={`${styles["Announcement-progress"]}`}
        style={{ animationDuration: `${duration}ms` }}
      />
    </div>
  );
};

 
export default Announcement;