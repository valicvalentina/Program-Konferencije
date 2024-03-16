import React from 'react';
import '../styles/Multimedia.css';
import FolderList from './FolderList';
import { Link } from 'react-router-dom';
import { Button } from 'semantic-ui-react';
import { FaPlusSquare } from 'react-icons/fa';
import axios from 'axios'
import {BsYoutube} from 'react-icons/bs'

export default function MultimediaFront(){
    const [numOfDays, setNumOfDays] = React.useState(0);
    const user = JSON.parse(localStorage.getItem('user'));
    const token = JSON.parse(localStorage.getItem('token'));

    const config= {
        headers: { Authorization : `Bearer ${token.token}`}
    }

    React.useEffect(() => {
        axios.get(`/api/multimedia/numberOfDays/${user.conferenceId}`,
                    config)
        .then(response =>  response.data)
        .then(days => {setNumOfDays(days); console.log(numOfDays)})
    }, []);

    
 return( 
    <div>
        <div className='multimedia-container'>   
            <Link to="/myConference"><Button color = 'teal' style={{width:200, margin:5}}>MY CONFERENCE</Button></Link>
            <div className='ytb-container'>
                <a href="https://youtube.com/playlist?list=PLRFR4J3sDD6CAiEn5uZ0eh4vYrUprccs_" target="_blank"><BsYoutube style={{height:100, width:100, color:'red', marginRight: 20}}></BsYoutube></a>   
                <p style={{marginRight: 50}}>Visit our YouTube channel</p>
            </div>
           
        </div>
        <h1>Welcome to multimedia!</h1>
        <hr></hr>

        {numOfDays==0 &&
        <div>
            <h2 style={{color:'red', marginLeft:20}}>No multimedia for this conference.</h2>
            <Link to="/addMultimediaDate"><Button color='teal' style={{width: 220, margin: 40}}>NEW MULTIMEDIA</Button></Link>
        </div>
        } 
        {numOfDays>0 &&
        <div>
            <FolderList></FolderList>
            {user.userAccountRole==='MAINADMIN' && 
                <Link to="/addMultimediaDate"><Button color='teal' style={{width: 220, margin: 40}}>NEW MULTIMEDIA</Button></Link>
            }
        </div>
        }
        
    </div>
 )
}