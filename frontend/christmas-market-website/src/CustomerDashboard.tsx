import React from 'react';
import Sidebar from './Sidebar';
import { Link } from 'react-router-dom';
import './CustomerDashboard.css';

import FamilyImage from './assets/351320-1600x1066-christmas-friends-1491333883 (1).jpg';
import MoneyImage from './assets/money.jpg';
import MarketImage from './assets/jinglebells-banner.jpg';
import GiftsImage from './assets/christmas-gift-bag-box-made-600nw-2502938927.webp';

const CustomerDashboard: React.FC = () => {
  const sections = [
    {
      image: FamilyImage,
      link: '/customers',
      label: 'Friends',
    },
    {
      image: MoneyImage,
      link: '/make-money',
      label: 'Make Money',
    },
    {
      image: MarketImage,
      link: '/market',
      label: 'Market',
    },
    {
      image: GiftsImage,
      link: '/inventory',
      label: 'Inventory',
    },
  ];

  return (
    <div className="dashboard-container">
      <Sidebar />
      <main className="dashboard-main">
        <div className="content-container">
          <div className="grid-container">
            {sections.map((section) => (
              <div key={section.label} className="grid-item">
                <Link
                  to={section.link}
                  className="image-link"
                >
                  <img
                    src={section.image}
                    alt={section.label}
                    className="dashboard-image"
                  />
                </Link>
                <span className="section-label">
                  {section.label}
                </span>
              </div>
            ))}
          </div>
          <h1 className="welcome-text">
            Welcome to the Christmas Market!
          </h1>
        </div>
      </main>
    </div>
  );
};

export default CustomerDashboard;
