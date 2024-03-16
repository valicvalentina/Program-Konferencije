import React, { useState } from 'react';
import '../styles/HomePage.css';
import { Link } from 'react-router-dom';
//import Login from './Login';
//import {BrowserRouter, Switch, Route}  from 'react-router-dom';
//import ReactDOM from 'react-dom/client';
import { Button} from 'semantic-ui-react';
import {
  MDBNavbar,
  MDBNavbarNav,
  MDBNavbarItem,
  MDBNavbarToggler,
  MDBContainer,
  MDBIcon,
  MDBCollapse,
} from 'mdb-react-ui-kit';
import ConferenceList from './ConferenceList';

export default function HomePage() {
  
 const [showBasic, setShowBasic] = useState(false);

 const logout = () => {
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    window.location.reload(false);
  };


  return (
    <header>
      <MDBNavbar expand='lg' light bgColor='primary'>
        <MDBContainer fluid>
        <MDBNavbarToggler
            onClick={() => setShowBasic(!showBasic)}
            aria-controls='navbarExample01'
            aria-expanded='false'
            aria-label='Toggle navigation'
          > 
          <MDBIcon fas icon='bars' />
          </MDBNavbarToggler>
          <MDBCollapse navbar show={showBasic}>
            <MDBNavbarNav right className='mb-2 mb-lg-0'>
              <MDBNavbarItem active>
              
              </MDBNavbarItem>
            </MDBNavbarNav>
          </MDBCollapse>
          <div className="button-header">
              {!localStorage.getItem('user') && 
                <div>
                  <Link to='/login'>
                    <Button className='button' color='teal' fluid size='large'>
                      Login
                    </Button>
                  </Link>
                </div>
              }
              {localStorage.getItem('user') && 
                <div className="button-container">
                  <Link to='/menu'>
                    <Button className='button' color='teal' fluid size='large'>
                      MENU
                    </Button>
                  </Link>
                  &nbsp;&nbsp;&nbsp;
                  <Link to='/'>
                    <Button className='button' color='teal' fluid size='large' onClick={logout}>
                      Logout
                    </Button>
                  </Link>
                </div>
              }
                
              </div>
        </MDBContainer>
      </MDBNavbar>

      <div
        className='p-5 text-center bg-image'
        style={{ backgroundImage: "url('https://mdbootstrap.com/img/new/slides/041.webp')", height: '400px' }}
      >
        <div className='mask' style={{ backgroundColor: 'rgba(0, 0, 0, 0.6)' }}>
          <div className='d-flex justify-content-center align-items-center h-100'>
            <div className='text-white'>
              <h1 className='mb-3 heading'>Change your life</h1>
              <h4 className='mb-3'>Join the best conferences in the world!</h4>
            </div>
          </div>
        </div>
      </div>
      <div className='text-conferences'>
        <div>
          <ConferenceList />
        </div>
      </div>
    </header>

  );
}

      