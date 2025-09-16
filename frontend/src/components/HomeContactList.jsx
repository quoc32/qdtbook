import React from 'react';
import style from './HomeContactList.module.css';

function ContactItem({ avatar, name, active }) {
  return (
    <div className={style.contact}>
      <div className={style.avatarWrapper}>
        <img src={avatar} alt={name} className={style.avatar} />
        {active && <span className={style.online}></span>}
      </div>
      <span className={style.name}>{name}</span>
    </div>
  );
}

export const ContactListHeaderButton = ({imageSrc, imageAlt, tooltipText}) => {
  return (
    <div className={style['contact-list-header-item-wrapper'] + " " + style["tooltip-container"]}>
      <img src={imageSrc} alt={imageAlt} className={style['contact-list-header-item']}/>
      <div className={style['tooltip-text']}>{tooltipText}</div>
    </div>
  );
}

const ContactListHeader = () => {
  return ( 
    <div className={"d-flex align-center justify-space-between border-bottom pb-1 m-0" + ' ' + style["contact-list-header"]}>
      <div style={{fontSize: '11px', fontWeight: '450'}}>Người liên hệ</div>
      <div>
        <ContactListHeaderButton imageSrc="search-alt.png" imageAlt="search alt" tooltipText={"Tìm kiếm theo tên hoặc nhóm"}/>
        <ContactListHeaderButton imageSrc="three-dot-1.png" imageAlt="3 dot" tooltipText={"Lựa chọn"}/>
      </div>

    </div>
  );
}

export default function ContactList() {
  return (
    <div>
      <aside className={`${style["sidebar"]} ${style["right"]}`}>
        <ContactListHeader />
        {Array.from({ length: 40 }).map((_, i) => (
          <ContactItem 
            avatar={"./user-avatar-1.png"}
            name={`User ${i + 1}`}
            active={i % 2 === 0}
            key={i}
          />
        ))}
      </aside>
    </div>
  );
}