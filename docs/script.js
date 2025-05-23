// Lista de usuarios conectados (simulado)
const usuarios = ["Carlos", "Ana", "Luis", "Romina"];

// Lista de mensajes simulada (en memoria)
let mensajes = [
    { usuario: "Carlos", contenido: "¡Hola a todos!" },
    { usuario: "Ana", contenido: "Hola Carlos 👋" }
];

// Modo demostración (sin backend)
const MODO_OFFLINE = true;

// Función para enviar mensaje
function enviarMensaje() {
    const texto = document.getElementById('mensaje').value;

    if (texto.trim() === "") return;

    if (MODO_OFFLINE) {
        mensajes.push({ usuario: "Carlos", contenido: texto });
        document.getElementById('mensaje').value = '';
        cargarMensajes();
        return;
    }

    fetch('/api/mensajes', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ usuario: "Carlos", contenido: texto })
    }).then(() => {
        document.getElementById('mensaje').value = '';
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
            chat.innerHTML += `<div><strong>${m.usuario}:</strong> ${m.contenido}</div>`;
        });
        return;
    }

    fetch('/api/mensajes')
        .then(res => res.json())
        .then(data => {
            chat.innerHTML = '';
            data.forEach(m => {
                chat.innerHTML += `<div><strong>${m.usuario}:</strong> ${m.contenido}</div>`;
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

