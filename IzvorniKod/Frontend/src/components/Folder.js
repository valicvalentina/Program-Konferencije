import React from "react";
import '../styles/Multimedia.css';
import { useHistory } from "react-router-dom";
import folderImg from './folder-icon.png';

function Folder(props) {
    const {date} = props;

    const history = useHistory();
    console.log("U FOLDERU:" + date);

    const toGallery=() => {
        history.push('/gallery', {state:{date:date}});
    }
    
    return (
        <div className='icong-container'><a onClick={() => {toGallery()}}><img src={folderImg}></img></a><p>{date.substring(0,10)}</p></div>
    );
}

export default Folder;