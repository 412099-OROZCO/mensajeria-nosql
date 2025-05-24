let nickname = "";
const MODO_OFFLINE = false;

// Mostrar modal de ingreso
function confirmarNickname() {
    const input = document.getElementById("nicknameInput");
    const valor = input.value.trim();

    if (!valor) {
        input.classList.add("is-invalid");
        return;
    }

    nickname = valor;
    document.getElementById("nicknameModal").remove();

    registrarUsuario();
    setInterval(registrarUsuario, 30000); // renovar conexión
    setInterval(cargarUsuarios, 5000);    // refrescar usuarios
    cargarMensajes();
    setInterval(cargarMensajes, 2000);    // refrescar mensajes
}

function registrarUsuario() {
    fetch('/api/usuarios', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ nickname })
    }).catch(err => console.error("❌ Error al registrar usuario:", err));
}

function enviarMensaje() {
    const texto = document.getElementById('mensaje').value.trim();
    const imagen = document.getElementById('imagen').value.trim();

    if (!texto && !imagen) return;

    const mensaje = {
        usuario: nickname,
        contenido: texto,
        imagenUrl: imagen
    };

    fetch('/api/mensajes', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(mensaje)
    }).then(() => {
        document.getElementById('mensaje').value = '';
        document.getElementById('imagen').value = '';
        cargarMensajes();
    }).catch(error => {
        console.error("Error al enviar mensaje:", error);
    });
}

function cargarMensajes() {
    const chat = document.getElementById('chat');

    fetch('/api/mensajes')
        .then(res => res.json())
        .then(data => {
            chat.innerHTML = '';
            data.forEach(m => {
                chat.innerHTML += `
          <div class="mb-2">
            <strong>${m.usuario}</strong> [${m.fechaHora || '--:--'}]: ${m.contenido}
            ${m.imagenUrl ? `<br><img src="${m.imagenUrl}" alt="imagen" style="max-width: 200px; border-radius: 8px; margin-top: 5px;">` : ''}
          </div>`;
            });
        })
        .catch(error => {
            console.error("Error al cargar mensajes:", error);
        });
}

function cargarUsuarios() {
    fetch('/api/usuarios')
        .then(res => res.json())
        .then(lista => {
            const ul = document.getElementById("usuarios");
            ul.innerHTML = "";
            lista.forEach(usuario => {
                const li = document.createElement("li");
                li.className = "list-group-item bg-dark text-light border-secondary";
                li.textContent = usuario;
                ul.appendChild(li);
            });
        })
        .catch(err => console.error("Error al cargar usuarios:", err));
}

document.addEventListener("DOMContentLoaded", () => {
    // El modal ya está visible al iniciar
});
