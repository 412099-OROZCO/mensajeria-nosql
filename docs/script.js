// Lista de usuarios conectados (simulado)
const usuarios = ["Carlos", "Ana", "Luis", "Romina"];

// Modo demostración
const MODO_OFFLINE = false;

// Función para enviar mensaje
function enviarMensaje() {
    const texto = document.getElementById('mensaje').value.trim();
    const imagen = document.getElementById('imagen').value.trim();

    if (!texto && !imagen) return;

    if (MODO_OFFLINE) {
        mensajes.push({ usuario: "Carlos", contenido: texto, imagenUrl: imagen, fechaHora: new Date().toLocaleTimeString() });
        document.getElementById('mensaje').value = '';
        document.getElementById('imagen').value = '';
        cargarMensajes();
        return;
    }

    const mensaje = {
        usuario: "Carlos",
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

// Función para cargar mensajes
function cargarMensajes() {
    const chat = document.getElementById('chat');

    if (MODO_OFFLINE) {
        chat.innerHTML = '';
        mensajes.forEach(m => {
            chat.innerHTML += `
                <div class="mb-2">
                    <strong>${m.usuario}</strong> [${m.fechaHora || '--:--'}]: ${m.contenido}
                    ${m.imagenUrl ? `<br><img src="${m.imagenUrl}" alt="imagen" style="max-width: 200px; border-radius: 8px; margin-top: 5px;">` : ''}
                </div>`;
        });
        return;
    }

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

// Función para mostrar usuarios conectados
function cargarUsuarios() {
    const ul = document.getElementById("usuarios");
    ul.innerHTML = "";
    usuarios.forEach(usuario => {
        const li = document.createElement("li");
        li.className = "list-group-item bg-dark text-light border-secondary";
        li.textContent = usuario;
        ul.appendChild(li);
    });
}

// Ejecutar al cargar el DOM
document.addEventListener("DOMContentLoaded", () => {
    cargarUsuarios();
    cargarMensajes();
    if (!MODO_OFFLINE) {
        setInterval(cargarMensajes, 2000);
    }
});


