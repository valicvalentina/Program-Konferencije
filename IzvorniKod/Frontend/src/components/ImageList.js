import React from "react";
import '../styles/Multimedia.css';
import ConferenceImage from "./ConferenceImage";
import axios from 'axios';

function ImageList(props) {
    const [images, setImages] = React.useState([]);

    var user = JSON.parse(localStorage.getItem('user'));
    const token = JSON.parse(localStorage.getItem('token'));
    const date = props.date;

    const data = {
        date: date //problem je kako doći do date
    }

    //console.log("DATA:" + data);

    const config= {
        headers: { Authorization : `Bearer ${token.token}`, 'Content-Type': 'application/json'}
    }

    React.useEffect(() => {
        axios.post(`/api/multimedia/getAllImagesForDate/${user.conferenceId}`,
                    JSON.stringify(data),
                    config)
        .then(response => setImages(response.data))
    }, []);
    return(
        <div className="gallery">
            {images.map(image => <ConferenceImage key={image.url} image={image}/>)}
        </div>
    );
}

export default ImageList;