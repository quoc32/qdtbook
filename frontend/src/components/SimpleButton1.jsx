
import React from 'react';
import style from './SimpleButton1.module.css';

const SimpleButton1 = ({imageSrc, imageAlt, tooltipText, noTooltip=false}) => {
  return (
    <div className={`${style['contact-list-header-item-wrapper']} ${style['tooltip-container']}`}>
      <img 
        src={imageSrc} 
        alt={imageAlt} 
        className={style['contact-list-header-item']}
      />
      {!noTooltip && (
        <div className={style['tooltip-text']}>{tooltipText}</div>
      )}
    </div>
  )
}
export default SimpleButton1;
