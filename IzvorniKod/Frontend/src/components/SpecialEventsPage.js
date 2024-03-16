import React from 'react';
import SpecialEventsList from './SpecialEventsList';
import {Button} from 'semantic-ui-react';
import { Link } from 'react-router-dom';
import '../styles/HomePage.css';
import { FaPlusSquare } from 'react-icons/fa';

export default function SpecialEventsPage() {
  var user = JSON.parse(localStorage.getItem('user'));
    return (
      <div>
          <div>
            <Link to='/myConference'>
              <Button color='teal' style={{width:200, margin: 10}}>
                  MY CONFERENCE
                </Button>
            </Link>
        </div>
        <h1 style={{margin:10}}>Special Events</h1>
        <hr/>
        <div className='text-special-events'>
          <div>
          <SpecialEventsList />
          </div>
        </div>
        {user.userAccountRole === "MAINADMIN" && 
          <div>
            <Link to="/createSpecialEvent"><Button color='teal' style={{width: 220, margin: 40}}>NEW SPECIAL EVENT</Button></Link>
          </div>
        }
        
      </div>
    );
}