
import './TabSelector1.css'

const TabSelector1 = ({ imgSrc = "./user-avatar-1.png", altText = "Tab Image", contentText = "Content" }) => {
  return ( 
    <div className="sidebar-item">
      <img
        src={imgSrc}
        alt={altText}
        className="sidebar-item__avatar"
      />
      <span className="sidebar-item__label">{contentText}</span>
    </div>
  );
}

 
export default TabSelector1;