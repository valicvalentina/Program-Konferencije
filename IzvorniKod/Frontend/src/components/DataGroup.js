import React from "react";
import '../styles/DataGroup.css';
import {AiFillFileText} from 'react-icons/ai'



function DataGroup(props) {
    var user = JSON.parse(localStorage.getItem('user'));
    const {groupName, data} = props.dataGroup;
    console.log(groupName);
    if(groupName !== 'Prezentacije predavanja' && groupName !== 'Zbornik radova' && groupName !== 'Raspored predavanja'){
        return (
            <div className="data-group-element">
                <p><b>{groupName}</b>  &emsp;   {data}</p>
            </div>
        ); 
    }

    if(groupName === 'Prezentacije predavanja' || groupName === 'Zbornik radova' || groupName === 'Raspored predavanja') {
        return(
            <div className="data-group-element">
                <p><b>{groupName}</b><a href={data} target="_blank">  &emsp; <AiFillFileText></AiFillFileText></a></p>
            </div>
        );
    }
    
}

export default DataGroup;