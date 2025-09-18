import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function QDTLogin() {
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPasword] = useState("");

  const handleLogin = async (e) => {
    e.preventDefault();
    // Perform login logic here (e.g., form validation, API call)
    console.log(email, password);

    try {
      const response = await fetch(import.meta.env.VITE_API_URL + "/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include", // gửi/nhận cookie JSESSIONID
        body: JSON.stringify({
          email,
          password, // backend của bạn đang nhận field này
        }),
      });

      if (!response.ok) {
        throw new Error("Sai email hoặc mật khẩu");
      }

      const data = await response.json();
      console.log("Login success:", data);
      // Backend trả về data.user với thông tin người dùng
      const userAuth = {
        id: data.user_id,
        email: data.email,
        gender: data.gender,
        bio: data.bio,
        full_name: data.full_name,
        first_name: data.first_name,
        last_name: data.last_name,
      };

      // Lưu sessionId hoặc user info vào localStorage nếu muốn
      localStorage.setItem("sessionId", data.session_id);
      sessionStorage.setItem("auth", JSON.stringify(userAuth));

      // Điều hướng về trang home
      navigate("/home");
    } catch (err) {
      console.error("Lỗi đăng nhập:", err);
    }
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
          <h3 className="text-primary fw-bold display-3 animate__animated animate__pulse">QDTbook</h3>
          <p className="fs-6">
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
                  placeholder="Email address"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                />
              </div>
              <div className="mb-3">
                <input
                  type="password"
                  className="form-control form-control-sm"
                  placeholder="Password"
                  value={password}
                  onChange={(e) => {setPasword(e.target.value)}}
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
