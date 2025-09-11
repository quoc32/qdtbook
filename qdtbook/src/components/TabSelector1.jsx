
import style from './TabSelector1.module.css'

const TabSelector1 = ({ imgSrc = "./user-avatar-1.png", altText = "Tab Image", contentText = "Content" }) => {
  return ( 
    <div className={style["sidebar-item"]}>
      <img
        src={imgSrc}
        alt={altText}
        className={style["sidebar-item__avatar"]}
      />
      <span className={style["sidebar-item__label"]}>{contentText}</span>
    </div>
  );
}

 
export default TabSelector1;