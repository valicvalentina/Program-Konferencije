import React from "react";
import SpecialEvent from "./SpecialEvent";
import axios from 'axios';


function SpecialEventsList() {
    const [events, setEvents] = React.useState([]);
    var user = JSON.parse(localStorage.getItem('user'));
    const token = JSON.parse(localStorage.getItem('token'));
    const config = {
        headers: { Authorization: `Bearer ${token.token}`}
      };
    React.useEffect(() => {
        axios.get( 
        `/api/specialEvents/${user.conferenceId}`,
        config
      ).then(response => {setEvents(response.data)}).catch(console.log);
        }, []);

    var numOfEvents = events.length;
    console.log(events);
    return(
        <div>
            {numOfEvents == 0 &&
                <h2 style={{color: "red", marginLeft:10}}>Sorry. No special events for this conference.</h2>
            }
            {numOfEvents!=0 &&
                <div className="conference">
                    {events.map(event => <SpecialEvent key={event.type} conference={event}/>)}
                </div>
            }
            
        </div>
        
    );
}

export default SpecialEventsList;