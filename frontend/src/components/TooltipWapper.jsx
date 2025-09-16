import React from 'react';
import style from './TooltipWapper.module.css';

const TooltipWapper = ({ tooltipText, noTooltip = false, additionalStyle, children }) => {
  return (
    <div className={`${style['contact-list-header-item-wrapper']} ${style['tooltip-container']}`}>
      {children}
      {!noTooltip && (
        <div className={style['tooltip-text']} style={additionalStyle}>{tooltipText}</div>
      )}
    </div>
  );
};

export default TooltipWapper;
