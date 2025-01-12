import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './Login';
import Register from './Register';
import { CustomerDashboard } from './pages/CustomerDashboard';
import { Customers } from './pages/Customers';
import { Market } from './pages/Market';
import { MoneyMaker } from './pages/MoneyMaker';
import { Inventory } from './pages/Inventory';
import { VendorDashboard } from './pages/VendorDashboard';
import { BuyFromStorage } from './pages/BuyFromStorage';
import { VendorInventory } from './pages/VendorInventory';
import { Loan } from './pages/Loan';
import { Stats } from './pages/Stats';

function App() {
  return (
    <Router>
      <div>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/dashboard" element={<CustomerDashboard />} />
          <Route path="/customers" element={<Customers />} />
          <Route path="/market" element={<Market />} />
          <Route path="/make-money" element={<MoneyMaker />} />
          <Route path="/inventory" element={<Inventory />} />
          <Route path="/vendor/dashboard" element={<VendorDashboard />} />
          <Route path="/vendor/storage" element={<BuyFromStorage />} />
          <Route path="/vendor/inventory" element={<VendorInventory />} />
          <Route path="/vendor/loan" element={<Loan />} />
          <Route path="/vendor/stats" element={<Stats />} />
          <Route path="*" element={<div>Page Not Found</div>} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
