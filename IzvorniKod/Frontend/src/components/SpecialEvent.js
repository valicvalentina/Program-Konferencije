import React, { useState } from "react";
import {Button, Popup} from 'semantic-ui-react';
import { useHistory } from 'react-router-dom';
import '../styles/Counter.css'
import '../styles/SpecialEvent.css';
import axios from 'axios';
import ShowAttendees from "./ShowAttendees";
import { useParams } from "react-router-dom/cjs/react-router-dom.min";

function SpecialEvent(props) {
    const {idSpecialEvent, capacity, type, message} = props.conference;

    const [pendingNumber, setPendingNumber] = useState();

    const [isAttending , setIsAttending] = useState();
    const [isPending, setIsPending] = useState();

    const user = JSON.parse(localStorage.getItem('user'));
    const token = JSON.parse(localStorage.getItem('token'));

    const config = {
        headers: { Authorization: `Bearer ${token.token}`}
    };

    React.useEffect(() => {
        axios.get( 
        `/api/specialEvents/isParticipantAttending/${idSpecialEvent}/${user.idUserAccount}`,
        config
      ).then(response => {setIsAttending(response.data)}).catch(console.log);
        }, []);

    React.useEffect(() => {
            axios.get( 
            `/api/specialEvents/isParticipantPending/${idSpecialEvent}/${user.idUserAccount}`,
            config
          ).then(response => {setIsPending(response.data)}).catch(console.log);
            }, []);

    React.useEffect(() => {
        axios.get( 
        `/api/specialEvents/pendingSize/${idSpecialEvent}`,
        config
      ).then(response => {setPendingText(response.data)}).catch(console.log);
        }, []);

    const apply = async() => {
        const data = {}
      
        const pConfig = {
            headers: { Authorization: `Bearer ${token.token}`, 'Content-Type': 'application/json'}
        };
        var response = await axios.post( 
            `/api/specialEvents/attend/${idSpecialEvent}/${user.idUserAccount}`,
             JSON.stringify(data),
             pConfig,
           ).then( function(response) {
                alert("You have successfully applied for special event.");
                setIsAttending(true);
           })
           .catch(function(error) {
            axios.post( 
                `/api/specialEvents/apply/${idSpecialEvent}/${user.idUserAccount}`,
                 JSON.stringify(data),
                 pConfig,
            )
            .then( function(response) {
                alert("All spots for this special event are already booked, but you will get the opportunity to attend as soon as there is a free spot.");
            setIsPending(true);
            })
            .catch( function (error) {
                alert('Something went wrong');
                console.log(error);
            })
           });
    }

    const [counter, setCounter] = React.useState(0);
    
 
    const increase = () => {
        setCounter(count => count + 1);
    };
 
    const decrease = () => {
        setCounter(count => count - 1 < 0 ? 0 : count - 1);
    };
 
    const send = async() =>{
        const data = {
            increase : counter
        }
        const pConfig = {
            headers: { Authorization: `Bearer ${token.token}`, 'Content-Type': 'application/json'}
        };
       await axios.post( 
            `/api/specialEvents/increasingCapacity/${idSpecialEvent}`,
             JSON.stringify(data),
             pConfig,
           )
        .catch((e) => {
            alert("Can't increase capacity because wait-list is empty.");
           })
        .then((response) => {
            console.log(response);
            if(response.status === 200) {
               setCapacityText(capacityText+counter);
               setPendingText(pendingText-counter < 0 ? 0 : pendingText-counter);
            } else {
                alert("Can't increase capacity because wait-list is empty.")
            }
        });
    }
    const [capacityText, setCapacityText] = React.useState(capacity);
    const [pendingText, setPendingText] = React.useState(pendingNumber);

    const history = useHistory();

    function ShowAttendees() {
        history.push(`/showAttendees/${idSpecialEvent}`);
    }

    return (
        <div>
        <div className="special-event-list-element">
            <p style={{margin:12}}><b>{type}</b></p>
            <div className="special-event-info">
                <p><span>capacity:</span> {capacityText}</p>
                <p><span>message:</span> {message}</p>
                {user.userAccountRole==='PARTICIPANT' && isAttending === false && isPending === false &&
                    <Button style={{width:100, margin: 10}} color='teal' onClick={apply}>
                        ATTEND
                    </Button>  
                }
                {
                    user.userAccountRole==='PARTICIPANT' && isAttending === true &&
                        <h5>You're attending this event</h5>
                }
                {
                    user.userAccountRole==='PARTICIPANT' && isPending === true &&
                        <h5>You are on the waiting list </h5>
                }
                {user.userAccountRole==='MAINADMIN' &&
                    <>
                        <div className="counter">
                        <p>Increase capacity:</p>
                        <div className="btn__container" te>
                            <Button className="control__btn" color = 'teal' onClick={decrease} style={{width: 20, height: 40}}>-</Button>
                            <span className="counter__output">{counter}</span>
                            <Button className="control__btn" onClick={increase} color = 'teal' style={{width: 20, height: 40}}>+</Button>
                        </div>
                        <Button color = 'teal' className="send" style={{marginLeft: 65, marginTop: 6}} onClick={send}>Update capacity</Button>
                        </div>   
                        <div>
                            <Button color = 'teal' className = "show" id={idSpecialEvent} onClick={ShowAttendees}>
                                Show Attendees
                            </Button>
                            <p>Number of pending participants: {pendingText}</p>
                        </div>
                    </>  
                }
               
            </div>
        </div>
        </div>
    );
}

export default SpecialEvent;