/*import React from 'react';
import axios from 'axios';
import { useState } from 'react';
import { Image } from 'cloudinary-react';

export default function Gallery() {

    const [imageSelected, setImageSelected] = useState("");

    const upload = () => {
        const formData = new FormData();
        formData.append("file", imageSelected);
        formData.append("upload_preset", "rndmbu2h");

        axios.post(
            "https://api.cloudinary.com/v1_1/dgkmcrdrh/image/upload",
            formData
        ).then((response) => console.log(response));
    }

    return (
        <div>
            <input type="file" onChange={(event) => setImageSelected(event.target.files[0])}></input>
            <button onClick={upload}>Upload image</button>

            <Image
                style={{width: 200}}
                cloudName="dgkmcrdrh"
                publicId=""
            ></Image>
        </div>
    )
}*/

import React from 'react';
import '../styles/Multimedia.css'
import ImageList from './ImageList';
import { useLocation } from 'react-router';
import { Link } from 'react-router-dom';
import { Button } from 'semantic-ui-react';
import axios from 'axios';
import { useState } from 'react';

export default function Gallery(props){
    const location = useLocation();
    const date = location.state.state.date;
    
    const [imageSelected, setImageSelected] = useState("");

    const user = JSON.parse(localStorage.getItem('user'));
    const token = JSON.parse(localStorage.getItem('token'));

    /*const fileRead = (event) => {
        setState({selectedFile: event.target.files[0]});
    } */

    /*const fileUpload = () => {
        const data = {
            date: '2022-12-25',
            url: '/' + state.selectedFile.name
        }
       
        const user = JSON.parse(localStorage.getItem('user'));
        console.log(user.conferenceId);
        const pConfig = {
            headers: { Authorization: `Bearer ${token.token}`, 'Content-Type': 'application/json'}
        };
        axios.post( 
            `/multimedia/add/${user.conferenceId}`,
             JSON.stringify(data),
             pConfig,
        ).then(response => {if(response.status===200) window.location.reload(true);});
    }; */

    const upload = () => {
        const formData = new FormData();
        formData.append("file", imageSelected);
        formData.append("upload_preset", "rndmbu2h");

        axios.post(
            "https://api.cloudinary.com/v1_1/dgkmcrdrh/image/upload",
            formData
        ).then((response) => {
            const data = {
                date: date,
                url: response.data.secure_url
            }

            console.log(data);

            const pConfig = {
                headers: { Authorization: `Bearer ${token.token}`, 'Content-Type': 'application/json'}
            };
            axios.post( 
                `/api/multimedia/add/${user.conferenceId}`,
                 JSON.stringify(data),
                 pConfig,
            ).then(response => {if(response.status===200) window.location.reload(true);});
        });
    }
    

  return( 
    <div>
        <Link to="/multimedia"><Button color = 'teal' style={{width:150, margin:5}}>MULTIMEDIA</Button></Link>
        <h1>GALLERY</h1>
        <hr></hr>
        <ImageList date={date}></ImageList>
        {user.userAccountRole==='MAINADMIN' &&
            <div className='upload-container'  style={{margin: 50}}>
                <input type="file" onChange={(event) => {setImageSelected(event.target.files[0])}}/>
                <Button color = 'teal' style={{marginLeft: 15}} onClick={upload}>UPLOAD</Button>
            </div>
        }
    </div>
 ) 
} 