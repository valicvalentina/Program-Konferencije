import React from "react";
import '../styles/Multimedia.css';
import axios from 'axios';
import Folder from "./Folder";


function FolderList() {
    const [dates,setDates] = React.useState([]);

    /*React.useEffect(() => {
        fetch('/active')
        .then(data =>  data.json())
        .then(conferences => setConferences(conferences))
    }, []); */
    var user = JSON.parse(localStorage.getItem('user'));
    const token = JSON.parse(localStorage.getItem('token'));

    const config= {
        headers: { Authorization : `Bearer ${token.token}`}
    }

    React.useEffect(() => {
        axios.get(`/api/multimedia/getDatesWithMultimedia/${user.conferenceId}`,
                    config)
        .then(response => setDates(response.data))
    }, []);

    return(
        <div className="folders">
            {dates.map(date => <Folder date={date}></Folder>)}
        </div>
    );
}

export default FolderList;