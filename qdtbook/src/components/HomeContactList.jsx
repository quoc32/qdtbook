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

export default function ContactList() {
  return (
    <div>
      <aside className={`${style["sidebar"]} ${style["right"]}`}>
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