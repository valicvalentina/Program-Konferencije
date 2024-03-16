import React from "react";
import '../styles/Multimedia.css';
import { Image } from "cloudinary-react";
import {FiDownload} from 'react-icons/fi'
import fileDownload from "js-file-download";
import axios from 'axios'
import { Button} from 'semantic-ui-react';

function ConferenceImage(props) {
    var user = JSON.parse(localStorage.getItem('user'));
    const {url} = props.image;
   /* return (
        <div>
            <img className='img-card'src={url}></img>
        </div>
    ); */
    const download = (url, filename) => {
        axios.get(
            url, {
            responseType: "blob"
        })
        .then((res) => {
            fileDownload(res.data, filename);
        });
    }
    return (
        <div className="image-container">
            <Image
                style={{width:200, margin:10}}
                cloudName="dgkmcrdrh"
                publicId={url}
            />
            <Button color='teal' style={{width:200, margin:10}} onClick={() => download(url, 'image.jpg')}>DOWNLOAD</Button>
            
        </div>
    )
}

export default ConferenceImage;