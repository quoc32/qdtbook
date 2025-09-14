import React from "react";
import { useNavigate } from "react-router-dom";

export default function QDTLogin() {
  const navigate = useNavigate();

  const handleLogin = (e) => {
    e.preventDefault();
    // Perform login logic here (e.g., form validation, API call)
    // On successful login, navigate to the home page
    navigate("/home");
  }

  const goToRegister = (e) => {
    e.preventDefault();
    navigate("/register");
  }

  return (
    <div className="container-fluid bg-light vh-100 d-flex align-items-center">
      <div className="row w-100 justify-content-center">
        {/* Left side */}
        <div className="col-md-5 d-flex flex-column justify-content-center mb-4 mb-md-0">
          <h1 className="text-primary fw-bold display-3 animate__animated animate__pulse">QDTbook</h1>
          <p className="fs-4">
            QDTbook helps you connect and share with the people in your life.
          </p>
        </div>

        {/* Right side (login card) */}
        <div className="col-md-4">
          <div className="card shadow p-4">
            <form>
              <div className="mb-3">
                <input
                  type="text"
                  className="form-control form-control-sm"
                  placeholder="Email address or phone number"
                />
              </div>
              <div className="mb-3">
                <input
                  type="password"
                  className="form-control form-control-sm"
                  placeholder="Password"
                />
              </div>
              <div className="d-grid mb-3">
                <button 
                  className="btn btn-primary btn-sm" 
                  onClick={handleLogin}
                  >
                    Log in
                  </button>  
                {/* //TODO: Add href to navigate to home page */}
              </div>
              <div className="text-center mb-3">
                <a href="#" className="text-decoration-none small">
                  Forgotten password?
                </a>
              </div>
              <hr />
              <div className="d-grid">
                <button 
                  className="btn btn-success btn-sm" 
                  onClick={goToRegister}
                  style={{ backgroundColor: "#00a400", borderColor: "#00a400" }}
                >
                  Create new account
                </button>
              </div>
            </form>
          </div>
          <p className="text-center mt-4 small">
            <a href="#" className="fw-bold text-dark text-decoration-none">
              Create a Page
            </a>{" "}
            for a celebrity, brand or business.
          </p>
        </div>
      </div>
    </div>
  );
}
